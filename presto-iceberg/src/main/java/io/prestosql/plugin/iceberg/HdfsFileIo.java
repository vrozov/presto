/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.prestosql.plugin.iceberg;

import io.prestosql.plugin.hive.HdfsEnvironment;
import io.prestosql.plugin.hive.HdfsEnvironment.HdfsContext;
import io.prestosql.spi.PrestoException;
import org.apache.hadoop.fs.Path;
import org.apache.iceberg.hadoop.HadoopInputFile;
import org.apache.iceberg.hadoop.HadoopOutputFile;
import org.apache.iceberg.io.FileIO;
import org.apache.iceberg.io.InputFile;
import org.apache.iceberg.io.OutputFile;

import java.io.IOException;

import static io.prestosql.plugin.hive.HiveErrorCode.HIVE_FILESYSTEM_ERROR;
import static java.util.Objects.requireNonNull;

public class HdfsFileIo
        implements FileIO
{
    private final HdfsEnvironment environment;
    private final HdfsContext context;

    public HdfsFileIo(HdfsEnvironment environment, HdfsContext context)
    {
        this.environment = requireNonNull(environment, "environment is null");
        this.context = requireNonNull(context, "context is null");
    }

    @Override
    public InputFile newInputFile(String pathString)
    {
        Path path = new Path(pathString);
        try {
            return HadoopInputFile.fromPath(path, environment.getFileSystem(context, path));
        }
        catch (IOException e) {
            throw new PrestoException(HIVE_FILESYSTEM_ERROR, "Failed to create input file: " + path, e);
        }
    }

    @Override
    public OutputFile newOutputFile(String pathString)
    {
        Path path = new Path(pathString);
        try {
            return HadoopOutputFile.fromPath(path, environment.getFileSystem(context, path));
        }
        catch (IOException e) {
            throw new PrestoException(HIVE_FILESYSTEM_ERROR, "Failed to create output file: " + path, e);
        }
    }

    @Override
    public void deleteFile(String pathString)
    {
        Path path = new Path(pathString);
        try {
            environment.getFileSystem(context, path).delete(path, false);
        }
        catch (IOException e) {
            throw new PrestoException(HIVE_FILESYSTEM_ERROR, "Failed to delete file: " + path, e);
        }
    }
}
